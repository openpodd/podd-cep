package podd

import com.espertech.esper.client.Configuration
import com.espertech.esper.client.EPAdministrator
import com.espertech.esper.client.EPServiceProvider
import com.espertech.esper.client.EPServiceProviderManager
import com.espertech.esper.client.EventSender
import com.espertech.esper.client.EventBean
import com.espertech.esper.client.UpdateListener

import javax.annotation.PostConstruct


class EsperService {
    EPServiceProvider epService
    EPAdministrator epAdmin

    @PostConstruct
    void init() {
        Configuration config = new Configuration()
        config.addImport(podd.PoddFunction.class)
        config.addPlugInSingleRowFunction('promoteState', podd.PoddFunction.class.getCanonicalName(), 'promoteState')


        epService = EPServiceProviderManager.getDefaultProvider(config);
        epService.initialize()
        epAdmin = epService.getEPAdministrator()
    }

    def createEPL(String epl) {
        def stmt = epAdmin.createEPL(epl)
        stmt.addListener(new UpdateListener() {
            @Override
            void update(EventBean[] newEvents, EventBean[] oldEvents) {
                // NOOP
            }
        })
        return stmt
    }

    def publishReport(String name, params) {
        EventSender sender = epService.getEPRuntime().getEventSender(name)
        sender.sendEvent(params)
    }
}
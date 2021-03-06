package podd

import com.espertech.esper.client.Configuration
import com.espertech.esper.client.EPAdministrator
import com.espertech.esper.client.EPServiceProvider
import com.espertech.esper.client.EPServiceProviderManager
import com.espertech.esper.client.EventSender
import com.espertech.esper.client.EventBean
import com.espertech.esper.client.UpdateListener
import com.espertech.esper.client.PropertyAccessException

import javax.annotation.PostConstruct
import java.lang.NullPointerException

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

class EsperService {
    EPServiceProvider epService
    EPAdministrator epAdmin
    def stmts

    @PostConstruct
    void init() {
        Configuration config = new Configuration()
        config.addImport(podd.PoddFunction.class)
        config.addPlugInSingleRowFunction('promoteState', podd.PoddFunction.class.getCanonicalName(), 'promoteState')


        epService = EPServiceProviderManager.getDefaultProvider(config);
        epService.initialize()
        epAdmin = epService.getEPAdministrator()

        stmts = [:]
    }

    def createEPL(String epl, String code) {

        def oldStmt = stmts.get(code)
        if (oldStmt) {
            println 'Exist stmt code: ' + code
            println 'Remove stmt code: ' + code
            oldStmt.removeAllListeners()
            oldStmt.destroy()
        }

        def stmt = epAdmin.createEPL(epl)
        stmt.addListener(new UpdateListener() {
            @Override
            void update(EventBean[] newEvents, EventBean[] oldEvents) {
                
                try {
                    if (newEvents.createReportUrl) {

                        def url = newEvents[0].createReportUrl;

                        println 'Listener: call api: ' + url
                        def http = new HTTPBuilder(url);
                        http.request(Method.POST) {

                            uri.query = [related_ids: newEvents.id]
                            
                            response.success = { resp ->
                                println "Success! ${resp.status}"
                            }
                            response.failure = { resp ->
                                println "Request failed with status ${resp.status}"
                            }
                        }
                        
                    }
                }
                catch (PropertyAccessException e) {
                }
                catch (NullPointerException e) {
                } 
            }
        })
        println 'createEPL: ' + code + ': ' + epl

        stmts[code] = stmt

        return stmt
    }

    def publishReport(String name, params) {
        EventSender sender = epService.getEPRuntime().getEventSender(name)
        sender.sendEvent(params)
        println 'publishReport'
    }

    def createSchema(String name, fields) {
        if (epAdmin.getConfiguration().isEventTypeExists(name)) {
            epAdmin.getConfiguration().updateMapEventType(name, fields)
        }
        else {
            epAdmin.getConfiguration().addEventType(name, fields)
        }

        println 'createSchema' + name
    }
}
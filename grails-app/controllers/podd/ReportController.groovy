package podd

import com.espertech.esper.client.EventTypeException
import java.util.Calendar;

// Event is reserve controller fixed by change to Report
class ReportController {

    static allowedMethods = [index:'POST']
    def esperService

    def index() {

        println 'Received request'

        def jsonObject = request.JSON
        
        try {
            jsonObject.data.timestamp = Calendar.getInstance().getTime().getTime()
            esperService.publishReport(jsonObject.name, jsonObject.data)
        } 
        catch (EventTypeException e) {
            render(status: 500, text: e)
        }
        
        render(contentType: "application/json") {
            [success: true]
        }
    }
}
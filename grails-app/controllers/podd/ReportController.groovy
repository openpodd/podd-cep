package podd

import com.espertech.esper.client.EventTypeException


// Event is reserve controller fixed by change to Report
class ReportController {

    static allowedMethods = [index:'POST']
    def esperService

    def index() {

        def jsonObject = request.JSON
        
        try {
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
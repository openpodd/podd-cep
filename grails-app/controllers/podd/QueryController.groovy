package podd

import com.espertech.esper.client.EPStatementException

class QueryController {

	static allowedMethods = [index:'POST']
	def esperService

	def index() {

		def jsonObject = request.JSON

		try {
			esperService.createEPL(jsonObject.stmt, jsonObject.code)
        } 
        catch (EPStatementException e) {
            render(status: 500, text: e)
        }

		render(contentType: "application/json") {
			[success: true]
		}
	}
}
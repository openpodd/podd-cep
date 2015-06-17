package podd

class QueryController {

	static allowedMethods = [index:'POST']
	def esperService

	def index() {
		def jsonObject = request.JSON
		esperService.createEPL(jsonObject.stmt)
		render(contentType: "application/json") {
			[success: true]
		}
	}
}
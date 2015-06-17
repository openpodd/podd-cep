package podd

class EventController {

	static allowedMethods = [index:'POST']
	def esperService

	def index() {
		def jsonObject = request.JSON
		// TODO: try exception
		esperService.publishReport(jsonObject.name, jsonObject.data)
		render(contentType: "application/json") {
			[success: true]
		}
	}
}
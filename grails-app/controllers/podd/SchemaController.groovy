package podd

class SchemaController {

	static allowedMethods = [index:'POST']
	def esperService

	def index() {
		def jsonObject = request.JSON
		esperService.createSchema(jsonObject.name, jsonObject.fields)

		render(contentType: "application/json") {
			[success: true]
		}
	}
}
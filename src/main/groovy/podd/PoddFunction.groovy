package podd

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.ContentType
import groovyx.net.http.Method

class PoddFunction {
	public static final int promoteState(int reportId, String responseUrlPattern) {
		def url = responseUrlPattern.replace('{{id}}', reportId.toString())
		println 'call api: ' + url

		def http = new HTTPBuilder(url)

		http.request(Method.POST) {
			response.success = { resp ->
		        println "Success! ${resp.status}"
		    }
		    response.failure = { resp ->
		        println "Request failed with status ${resp.status}"
		    }
		}
		return reportId
	}
}
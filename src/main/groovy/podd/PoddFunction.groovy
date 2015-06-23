package podd

class PoddFunction {
	public static final int promoteState(int reportId, String state) {
		println 'call api reportId: ' + reportId + ', state: ' + state
		return reportId
	}
}
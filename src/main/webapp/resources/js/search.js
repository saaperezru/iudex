/* 
 */
function searchStarted(){
	$("#DataSearch").parent().spin("large");
}
function searchCompleted(){
	$("#DataSearch").parent().spin(false);
}

function search(data){

	var ajaxstatus = data.status; // Can be "begin", "complete" and "success"

	switch (ajaxstatus) {
		case "begin": // This is called right before ajax request is been sent.
			searchStarted();
			break;

		case "success": // This is called when ajax response is successfully processed.
			searchCompleted();
			break;

		case "complete": // This is called right after ajax response is received.
			// NOOP.
			break;
	}
}


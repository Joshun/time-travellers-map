function issuesOnPage(labelCounts) {
    $("#bug-count").text(labelCounts.bug);
    $("#feature-count").text(labelCounts.enhancement);
    $("#pullreq-count").text(labelCounts.pull_request);
}

function contributorsLoaded(data) {
    console.log(data);
    $("#contrib-count").text(data.length);
}

function issuesLoaded(data) {
    labels = {"pull_request": 0};

    for (issue in data) {
        // console.log(data[issue]);
        if ("pull_request" in data[issue]) {
            labels["pull_request"] += 1;
        }

        for (label in data[issue].labels) {
            // console.log(data[issue].labels[label].name)
            var labelStr = data[issue].labels[label].name;
            
            if (labelStr in labels) {
                labels[labelStr] += 1;
            }
            else {
                labels[labelStr] = 1;
            }
        }
    }

    issuesOnPage(labels);
    
}


$.get(
    "https://api.github.com/repos/Joshun/time-travellers-map/issues",
    function(data) {
        issuesLoaded(data)
    }
);

$.get(
    "https://api.github.com/repos/Joshun/time-travellers-map/contributors",
    function(data) {
        contributorsLoaded(data);
    }
);
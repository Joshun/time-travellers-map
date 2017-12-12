function issuesOnPage(labelCounts) {
    $("#bug-count").text(labelCounts.bug);
    $("#feature-count").text(labelCounts.enhancement);
}

function issuesLoaded(data) {
    labels = {};

    for (issue in data) {
        // console.log(data[issue]);
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
function releaseLoaded(data) {
    var releaseNum = data["tag_name"];
    var releaseLink = data["zipball_url"];
    var releaseName = data["name"];

    var linkElement = $("<a>", {
        title: "Download latest release",
        text: "source code",
        href: releaseLink
    });



    $("#latest-release-bar").append(linkElement);
    $("#latest-release-header").text(releaseNum + " " + releaseName);
    console.log("release info loaded.");
    console.log(releaseLink);
}


$.get(
    "https://api.github.com/repos/Joshun/time-travellers-map/releases/latest",
    function(data) {
        releaseLoaded(data);
    }
)

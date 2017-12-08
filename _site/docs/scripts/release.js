function releaseLoaded(data) {
    var releaseNum = data["tag_name"];
    var releaseLink = data["zipball_url"];

    var linkElement = $("<a>", {
        title: releaseNum,
        text: "Download latest release",
        href: releaseLink
    })
    $("#latest-release-bar").append();
    console.log("release info loaded.");
}


$.get(
    "https://api.github.com/repos/Joshun/time-travellers-map/releases",
    function(data) {
        releaseLoaded(data);
    }
)

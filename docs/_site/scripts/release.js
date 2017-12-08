function releaseLoaded(data) {
    var releaseNum = data["tag_name"];
    var releaseSrcLink = data["zipball_url"];
    var releaseBinLink = data["assets"][0]["browser_download_url"];
    var releaseName = data["name"];

    var linkList = $("<ul>");

    var srcLinkElement = $("<a>", {
        title: "Download latest release",
        text: "source code",
        href: releaseSrcLink
    });

    var binLinkElement = $("<a>", {
        title: "Download latest release",
        text: "JAR",
        href: releaseBinLink
    });

    linkList.append($("<li>").append(binLinkElement));    
    linkList.append($("<li>").append(srcLinkElement));


    $("#latest-release-bar").append(linkList);
    $("#latest-release-header").text(releaseNum + " " + "\"" + releaseName + "\"");
    console.log("release info loaded.");
    console.log(releaseLink);
}


$.get(
    "https://api.github.com/repos/Joshun/time-travellers-map/releases/latest",
    function(data) {
        releaseLoaded(data);
    }
)

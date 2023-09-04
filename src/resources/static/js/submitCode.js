function send() {
    let views = document.getElementById("views_restriction").value;
    let time = document.getElementById("time_restriction").value
    let object = {
        "code": document.getElementById("code_snippet").value,
        "maxViews": views > 0 ? views : 0,
        "maxViewTime": time > 0 ? time : 0,
        "viewsAreRestricted": views > 0 ? true : false;
        "viewTimeIsRestricted": time > 0 ? true, false;
    };

    let json = JSON.stringify(object);

    let xhr = new XMLHttpRequest();
    xhr.open("POST", '/api/code/new', false)
    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
    xhr.send(json);

    if (xhr.status == 200) {
      alert("Success!");
    }
}
function addStatus(name, ipAddr){
    let node=
            `<tr id="status-${name}">
                <td>${name}</td>
                <td>${ipAddr}</td>
                <td><span class="badge">running</span></td>
            </tr>`
    $("#all-status").append(node);
}

function alterStatus(name, status){
    $(`#status-${name}`).find("span").text(status);
}

function addGraph(name){
    let node = 
            `<div class="card">
                <div class="card-header" id="graph-head-${name}">
                    <button class="btn btn-link" type="button" data-toggle="collapse"
                        data-target="#graph-body-${name}" aria-expanded="true" aria-controls="graph-body-${name}">
                    ${name}
                    </button>
                </div>

                <div id="graph-body-${name}" class="collapse" aria-labelledby="graph-head-${name}" data-parent="#all-graphs">
                    <div class="card-body">
                        <div id="graph-${name}"></div>
                    </div>
                </div>
            </div>`
    $("#all-graphs").append(node);
}
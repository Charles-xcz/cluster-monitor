let allData = [];
let nts = [];

let layout = {
    title: '系统负载图',
    xaxis: {
        zeroline: true,
        rangemode: 'nonegative',
        type: 'date',
        title: {
            text: '时间'
        }
    },
    yaxis: {
        zeroline: true,
        range: [0, 100],
        rangemode: 'nonegative',
        title: {
            text: '利用率(%)'
        }
    },
    legend: {
        y: 0.5,
        traceorder: 'reversed',
        font: {
            size: 16
        }
    }
}

function handleNewMsg(data) {
    data = JSON.parse(data);
    if(data instanceof String){
        return;
    }
    let x;
    if (allData !== []) {
        x = allData.find((x) => x.name === data.name);
    }
    if (!x) {
        let xaxis = new Array(60).fill(data.time);
        for (let i = xaxis.length - 2; i > 0; i--)
            xaxis[i] = xaxis[i + 1] - 2000;
        let d = {
            name: data.name,
            data: {
                x: xaxis,
                y: new Array(60).fill(null),
                mode: 'lines',
                type: 'scatter'
            },
        }
        x = d;
        addStatus(data.name, data.ipAddr);
        addGraph(data.name);
        allData.push(x);
    }
    x.data.x.shift();
    x.data.x.push(data.time);
    x.data.y.shift();
    x.data.y.push(data.heart);
    draw('graph-' + x.name, x.data);
}

function draw(id, data) {
    Plotly.newPlot(id, [data], layout);
}

// setInterval(() => {
//     handleNewMsg({
//         name: 'abcd',
//         time: Date.now(),
//         heart: Math.random() * 100
//     })
// }, 1000)

function scanStatus() {
    for (let d of allData) {
        if (d.data.x.length > 0) {
            let last = d.data.x[d.data.x.length - 1];
            let data = nts.find((x) => x.name === d.name)
            if (data) {
                if (data.timestamp === last) {
                    alterStatus(data.name, 'failed');
                } else {
                    data.timestamp = last;
                    alterStatus(data.name, 'running')
                };
            } else nts.push({
                name: d.name,
                timestamp: last
            });
        }
    }
}

setInterval(scanStatus, 6000);
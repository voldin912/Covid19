const target = 100;
const dailyTotal = data.prefs.reduce((a, b) => a + b.patients.slice(-1)[0], 0);
var graphFontColor = 'rgba(255, 255, 255, .8)';
var axisLineColor = 'rgba(255, 255, 255, .2)';
Chart.defaults.global.defaultFontColor = graphFontColor;
$("#update").text(data.lastUpdate);
var c = $("#data");
var row = $('<div class="tableRow"></div>');
var firstrow = row;
var x = 1;
var areas = [];
var charts = [];
var total = Array(data.prefs[0].patients.length);
total.fill(0);
var motarity = Array(data.prefs[0].motarity.length);
var hospitalizations = Array(data.prefs[0].hospitalizations.length);
motarity.fill(0);
hospitalizations.fill(0);
var wholeJapan = [];
var wholeJapanPerPopulation = [];
var wholePopulation = 0;
data.prefs.forEach(pref => {
    var slice = pref.patients.slice(-3);

    for (var i = 0; i < pref.patients.length; ++i) {
        total[i] += pref.patients[i];
        motarity[i] += pref.motarity[i];
        hospitalizations[i] += pref.hospitalizations[i];
    }

    // map data
    var rate;

    /* old map until 7/16
    var pre = slice[0];
    var latest = slice[2];
    const thresould = 1.5;
    */
    
    var weekdata = pref.patients.slice(-31);
    var pre = weekdata[28] - weekdata[0];
    var latest = weekdata[30] - weekdata[0];
    const thresould = 3;
    
    if (pre === 0) {
        if (latest > 0) {
            rate = thresould;
        } else {
            rate = 1;
        }
    } else {
        rate = latest / pre;
    }

    wholeJapan.push({pref: pref.pref, patient: latest});
    wholeJapanPerPopulation.push({pref: pref.pref, patient: latest * 100 / pref.population});
    wholePopulation += pref.population;

    if (rate > thresould) {
        rate = 1;
    } else {
        rate = (Math.sqrt(rate) - 1) / (Math.sqrt(thresould) - 1);
    }
    if (rate < 0) {
        rate = 0;
    }
    areas.push({
        code: pref.code,
        jp: pref.pref,
        color: "rgba(" + Math.floor(255 * rate) + "," + Math.floor(255 * (1-rate)) + ",0,.8)",
        prefectures: [pref.code]
    });

    var motal = pref.motarity.slice(-1)[0];
    [div, chart] = createChart(pref.pref, pref.dates, latest, motal, pref.patients, pref.hospitalizations, pref.motarity, pref.population, true);
    row.append(div);
    charts.push(chart);
    ++x;
    if (x === 5) {
        x = 0;
        c.append(row);
        row = $('<div class="tableRow"></div>');
    }
});
if (x > 0) {
    c.append(row);
}

var post = total.slice(-1)[0];
var mot = motarity.slice(-1)[0];
[div, chart] = createChart("全国", data.prefs[0].dates, post, mot, total, hospitalizations, motarity, wholePopulation, false);
charts.push(chart);
firstrow.prepend(div);

$("#map").japanMap({
    areas: areas,
    movesIslands: true,
    height: 300
});

wholeJapan.sort((a, b) => b.patient - a.patient);
wholeJapanPerPopulation.sort((a, b) => b.patient - a.patient);

var wholeJapanPrefs = [];
var wholeJapanPatients = [];
wholeJapan.forEach(p => {
    wholeJapanPrefs.push(p.pref);
    wholeJapanPatients.push(p.patient);
});

wholeJapanPopPrefs = [];
wholeJapanPopPatients = [];
wholeJapanPerPopulation.forEach(p => {
    wholeJapanPopPrefs.push(p.pref);
    wholeJapanPopPatients.push(Math.round(p.patient * 10) / 10);
});

var prefsChart = new Chart(document.getElementById("bar").getContext("2d"), {
    type: 'bar',
    data: {
        labels: wholeJapanPrefs,
        datasets: [{
                label: "感染者数",
                data: wholeJapanPatients,
                backgroundColor: "rgba(0, 255, 0, 0.5)"
        }]
    },
    options: {
        legend: {
           display: false
       },
       scales: {
           xAxes: [
                {
                    gridLines: {
                        display: false
                    },
                    ticks: {
                        maxRotation: 90
                    }
                }],
            yAxes: [
                {
                    gridLines: {
                        color: axisLineColor,
                        zeroLineColor: graphFontColor
                    }
                }
            ]
       }
    }
});

$('input[name="population"]:radio').change(populationChanged);

typeChanged();
populationChanged();

function populationChanged() {
    if ($('input[name="population"]:checked').val() === '2') {
        prefsChart.data.labels = wholeJapanPopPrefs;
        prefsChart.data.datasets[0].data = wholeJapanPopPatients;
        $('#bar-title').text("10万人あたり感染者数");
    } else {
        prefsChart.data.labels = wholeJapanPrefs;
        prefsChart.data.datasets[0].data = wholeJapanPatients;
        $('#bar-title').text("感染者数");
    }
    prefsChart.update();
}

function typeChanged(){
    if ($('input[name="calc"]:checked').val() === '2') {
        offset = 0;
        labelOffset = 0;
        logMax = 4000;
    } else {
        offset = 2;
        labelOffset = 1;
        logMax = 300;
    }
    if ($('input[name="axis"]:checked').val() === '2') {
        type = 'logarithmic';
    } else {
        type = 'linear';
    }
    prefsChart.options.scales.yAxes[0].type = type;
    prefsChart.update();

    var sel = $('input[name="type"]:checked').val();
    charts.forEach(ch => {
        switch (sel) {
            case '1':
                ch.data.datasets = [ch.data.datapool[0 + offset], ch.data.datapool[1 + offset]];
                if (offset === 2) {
                    ch.data.datasets.push(ch.data.datapool[5]);
                }
                break;
            case '2':
                ch.data.datasets = [ch.data.datapool[0 + offset]];
                if (offset === 2) {
                    ch.data.datasets.push(ch.data.datapool[5]);
                }
                break;
            case '3':
                ch.data.datasets = [ch.data.datapool[1 + offset]];
                break;
            case '4':
                ch.data.datasets = [ch.data.datapool[4]];
                labelOffset = 7;
                type = 'linear';
                break;
            case '5':
                ch.data.datasets = [ch.data.datapool[6]];
                break;
        }
        if (type === 'logarithmic' && ch.options.pref) {
            ch.options.scales.yAxes[0].ticks.max = logMax;
        } else {
            delete ch.options.scales.yAxes[0].ticks.max;
        }
        ch.options.scales.yAxes[0].type = type;
        ch.data.labels = ch.data.dateLabels.slice(labelOffset);
        ch.update();
    });
}

$('input[name="type"]:radio').change(typeChanged);
$('input[name="calc"]:radio').change(typeChanged);
$('input[name="axis"]:radio').change(typeChanged);

function addSpan(div, cls, text) {
    var s = $('<span></span>');
    s.addClass(cls);
    s.text(text);
    div.append(s);
}

function createChart(name, dates, infect, motal, patients, hospitalizations, motarity, population, pref) {
    var weekData = patients.slice(-8);
    var weekAgo = weekData[0];
    /*
    var rateText;
    if (weekAgo === 0) {
        rateText = "-";
    } else {
        rateText = (Math.round(infect / weekAgo * 100) / 100);
    }*/

    var div = $('<div class="chartCell"></div>');
    var prefLabel = $('<div></div>');
    addSpan(prefLabel, "pref-name", name);
    addSpan(prefLabel, "value-label", "(感染:");
    addSpan(prefLabel, "value", infect);
    addSpan(prefLabel, "value-label", " 死者:");
    addSpan(prefLabel, "value", motal);
    addSpan(prefLabel, "value-label", ")");
    div.append(prefLabel);
    var rateDiv = $('<div></div>');
    var daily = weekData[7] - weekData[6];
    /*
    addSpan(rateDiv, "rate-value", rateText);
    addSpan(rateDiv, "rate-label", "倍/週");
    */
    addSpan(rateDiv, "rate-label", " 新規:");
    addSpan(rateDiv, "rate-value", daily);
    addSpan(rateDiv, "rate-label", "人 週");
    addSpan(rateDiv, "rate-value", Math.round((weekData[7] - weekAgo) * 100 / population * 100) / 100);
    addSpan(rateDiv, "rate-label", "人/10万");
    /*
    addSpan(rateDiv, "rate-label", " (目標: ");
    addSpan(rateDiv, "rate-label", Math.round((weekData[7] * target / dailyTotal) * 10) / 10);
    addSpan(rateDiv, "rate-label", ")");
     */
    div.append(rateDiv);
    var canv =  $('<canvas></canvas');
    div.append(canv);
    row.append(div);

    var ctx = canv.get(0).getContext("2d");
    var patdif = new Array();
    var deathdif = new Array();
    var rateInWeek = new Array();
    var patavg = [];
    for (var i = 1; i < patients.length; ++i) {
        patdif.push(Math.max(patients[i] - patients[i - 1], 0));
        if (i <= 12) {
            deathdif.push(0); // until 3/20
        } else {
            deathdif.push(Math.max(motarity[i] - motarity[i - 1], 0));
        }
        if (i >= 7) {
            if (patients[i - 7] > 0) {
                rateInWeek.push(patients[i] / patients[i - 7]);
            } else {
                rateInWeek.push(0);
            }
        }
        var min = Math.max(0, i - 7);
        patavg.push(Math.max(0, patients[i] - patients[min]) / (i - min));
    }
    var chart = new Chart(ctx, {
       type: 'bar',
       data: {
           dateLabels : dates,
           datapool: [{
                   type: 'line',
                   label: "感染者",
                   data: patients,
                   backgroundColor: "rgba(153,255,51,0.6)",
                   pointRadius: 0,
                   pointHitRadius: 3
                },
                {
                   type: 'line',
                   label: "死者",
                   data: motarity,
                   backgroundColor: "rgba(100,0,0,1)",
                   pointRadius: 0,
                   pointHitRadius: 3

                },
                {
                   type: 'bar',
                   label: "感染者",
                   data: patdif,
                   backgroundColor: "rgba(0,200, 0,1)"
                },
                {
                   type: 'bar',
                   label: "死者",
                   data: deathdif,
                   backgroundColor: "rgba(255,53,51,1)"
                },
                {
                   type: 'line',
                   label: "増加率",
                   data: rateInWeek,
                   borderColor: "rgba(200, 255, 200, 0.8)",
                   fill: false,
                   pointRadius: 0,
                   pointHitRadius: 3
                },
                {
                    type: 'line',
                    label: "移動平均",
                    data: patavg,
                    borderColor: "rgba(225, 120, 255, 0.4)",
                    fill: false,
                    pointRadius: 0,
                    pointHitRadius: 0
                },
                {
                    type: 'line',
                    label: "入院",
                    data: hospitalizations,
                    backgroundColor: "rgba(120,130,255,1)",
                    pointRadius: 0,
                    pointHitRadius: 3
                }
           ]
       },
       options: {
           pref: pref,
           legend: {
               display: false
           },
           scales: {
               yAxes: [{
                       gridLines: {
                           color: axisLineColor,
                           zeroLineColor: axisLineColor
                       },
                       ticks: {
                           beginAtZero: true
                       },
                       type: 'linear'
               }],
               xAxes: [{
                   display: false
               }]

           }
       }
    });
    return [div, chart];
}

var series;

$(document).ready(

		function() {

			console.log("ready");

			var wsUri = getRootUri() + "/energymgr/echo";

			function getRootUri() {
				return "ws://"
						+ (document.location.hostname == "" ? "localhost"
								: document.location.hostname)
						+ ":"
						+ (document.location.port == "" ? "8080"
								: document.location.port);
			}

			function init() {
				output = document.getElementById("output");
			}

			function send_echo() {
				websocket = new WebSocket(wsUri);
				websocket.onopen = function(evt) {
					onOpen(evt);
				}
				websocket.onmessage = function(evt) {
					onMessage(evt);
				}
				websocket.onerror = function(evt) {
					onError(evt);
				}
			}

			function onOpen(evt) {
				writeToScreen("CONNECTED");
				// doSend(textID.value);

			}

			function onMessage(evt) {
				writeToScreen("RECEIVED: " + evt.data);

				if (evt.data !== 'Connection Established') {
					var data = JSON.parse(evt.data);
					var a = (new Date(data.date)).getTime();
					var b = parseFloat(data.kwh);

					$.growl.notice({
						message : b + ' kwh',
						title : data.date,
						location : 'br'
					});

					console.log(a + " " + b);

					series.addPoint([ a, b ], true, true);
				}
			}

			function onError(evt) {
				writeToScreen('<span style="color: red;">ERROR:</span> '
						+ evt.data);
			}

			function doSend(message) {
				writeToScreen("SENT: " + message);
				websocket.send(message);
			}

			function writeToScreen(message) {
				var pre = document.createElement("p");
				pre.style.wordWrap = "break-word";
				pre.innerHTML = message;
				// alert(output);
				output.appendChild(pre);
			}

			$("#sendecho").click(function(e) {
				send_echo();
			});

			window.addEventListener("load", init, false);

			Highcharts.setOptions({
				global : {
					useUTC : false
				}
			});

			// Create the chart
			Highcharts.stockChart('container', {
				chart : {
					events : {
						load : function() {
							series = this.series[0];

							send_echo();

						}
					}
				},

				rangeSelector : {
					buttons : [ {
						count : 8,
						type : 'hour',
						text : '8H'
					}, {
						type : 'all',
						text : 'All'
					} ],
					inputEnabled : false,
					selected : 0
				},

				title : {
					text : 'Live energy data'
				},

				exporting : {
					enabled : false
				},

				series : [ {
					name : 'kwh',
					data : (function() {
						// generate an array of random data
						var data = [], time = (new Date()).getTime(), i;

						for (i = -24; i <= 0; i += 1) {
							data.push([ time + i * 1000 * 60 * 60, 0 ]);
						}
						return data;

					}())
				} ]
			});

		});
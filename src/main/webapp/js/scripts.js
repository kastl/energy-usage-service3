$(document).ready(function() {

	console.log("ready");

	var days;
	var days2;

	var energies;
	var energies2;

	var myAPI = "http://192.168.99.100:8080/resources/api/load";
	var myAPI2 = "http://192.168.99.100:8080/resources/api/weekly";
	var myAPI3 = "http://192.168.99.100:8080/resources/api/daily";

	$("#load").click(function(e) {

		e.preventDefault();

		$('#result').html("Reloading...");

		$.getJSON(myAPI, function(jsonResult) {
			console.log("--begin call to " + myAPI);
		}).done(function(data) {
			console.log(data);

			$('#result').html("Using newly loaded data");
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.log('--getJSON request failed. Status: ' + textStatus);
		}).always(function() {
			console.log('--end');
		});
	});

	$("#loadCharts").click(function(e) {
		e.preventDefault();

		$.getJSON(myAPI2, function(jsonResult) {
			console.log("--begin call to " + myAPI);
		}).done(function(data) {
			console.log(data);

			days = data.map(function(x) {
				return x.start;
			});
			energies = data.map(function(x) {
				return x.energy;
			});
			createChart();
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.log('--getJSON request failed. Status: ' + textStatus);
		}).always(function() {
			console.log('--end');
		});

		$.getJSON(myAPI3, function(jsonResult) {
			console.log("--begin call to " + myAPI);
		}).done(function(data) {
			console.log(data);

			days2 = data.map(function(x) {
				return x.hour;
			});
			energies2 = data.map(function(x) {
				return x.energy;
			});
			createChart2();
		}).fail(function(jqXHR, textStatus, errorThrown) {
			console.log('--getJSON request failed. Status: ' + textStatus);
		}).always(function() {
			console.log('--end');
		});

	});

	var createChart = function() {
		var ctx = document.getElementById('myChart').getContext('2d');
		var chart = new Chart(ctx, {
			// The type of chart we want to create
			type : 'bar',

			// The data for our dataset
			data : {

				labels : days,

				datasets : [ {
					label : "Energy",
					backgroundColor : 'rgb(255, 99, 132)',
					borderColor : 'rgb(255, 99, 132)',
					data : energies,

				} ]
			},

			options : {
				scales : {
					yAxes : [ {
						scaleLabel : {
							display : true,
							labelString : 'kWh'
						}
					} ],
					xAxes : [ {
						scaleLabel : {
							display : true,
							labelString : 'Day'
						}
					} ]
				}
			}
		});
	}

	var createChart2 = function() {
		var ctx = document.getElementById('myChart2').getContext('2d');
		var chart = new Chart(ctx, {
			// The type of chart we want to create
			type : 'line',

			// The data for our dataset
			data : {
				labels : days2,
				datasets : [ {
					label : "Energy",
					backgroundColor : 'rgb(255, 99, 132)',
					borderColor : 'rgb(255, 99, 132)',
					data : energies2,
				} ]
			},

			options : {
				scales : {
					yAxes : [ {
						scaleLabel : {
							display : true,
							labelString : 'kWh'
						}
					} ],
					xAxes : [ {
						scaleLabel : {
							display : true,
							labelString : 'Hour'
						}
					} ]
				}
			}
		});
	}

});
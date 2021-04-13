//modulo che gestisce l'anagrafica 
var app = angular.module('anagrafica', [ 'ngAnimate', 'ui.bootstrap' ]);

// rimetto scope perchè liferay minimizza il codice javascript mandando a
// puttane la variabile scope
// controller per la parte anagrafica del'utente dati etc etc)
app
		.controller(
				'anaCtrl',
				[
						"$scope",
						"$http",
						function($scope, $http) {
							// carico dentro lo scope l'elenco delle uot.

							$scope.elencoUot = wcs_anagraficaUOTArray;

							// Any function returning a promise object can be
							// used to load values asynchronously

							$scope.testFunction = function() {

								$http(
										{
											method : 'POST',
											url : '/CartellaSociale/popola',
											data : 'codAnag=6340807&action=anagrafica',
											headers : {
												'Content-Type' : 'application/x-www-form-urlencoded'
											}
										}).

								then(function(response) {

									if (response.data.success == true) {
										$scope.utente = response.data.data;

									} else {
										alert("errore");
									}
									console.log($scope.utente);
									// this callback will be called
									// asynchronously
									// when the response is available
								}, function(response) {
									console.log('KO ' + response);
								});

							};
							// Funzione per recuperare gli stati di nascita
							$scope.recuperaStati = function(val) {
								return $http.get('/CartellaSociale/stato', {
									params : {
										query : val
									}
								}).then(function(response) {
									return response.data.stato

									console.log(response.data.stato);
									/*
									 * return
									 * response.data.results.map(function(item){
									 * return item.formatted_address; });
									 */
								});

							};

							// Funzione per recuperare provincie
							$scope.recuperaProvincie = function(val, codStato) {
								console.log(codStato);
								if (codStato == 100) {
									return $http.get(
											'/CartellaSociale/provincia', {
												params : {
													codStato : codStato,
													query : val
												}
											}).then(function(response) {
										return response.data.data

										/*
										 * return
										 * response.data.results.map(function(item){
										 * return item.formatted_address; });
										 */
									});
								} else {
									provincia = [ {
										'codProvincia' : val,
										'desProvincia' : val
									} ];
									return provincia;
								}

							};

							// Funzione per recuperare provincie
							$scope.recuperaComuni = function(val, codStato,
									codProvincia) {
								console.log(codStato);
								if (codStato == 100) {
									return $http.get('/CartellaSociale/comune',
											{
												params : {
													codStato : codStato,
													codProv : codProvincia,
													query : val
												}
											}).then(function(response) {
										return response.data.comune

										/*
										 * return
										 * response.data.results.map(function(item){
										 * return item.formatted_address; });
										 */
									});
								} else {
									comune = [ {
										'codComune' : val,
										'desComune' : val
									} ];

									return comune;
								}

							};

							// funzione per il select stato di nascita
							$scope.selectStatoNascita = function($item, $model,
									$label) {
								$scope.utente.anagraficaStatoNascita = $model.codStato
								// se cambia lo stato di nascita resetto tutto.
								$scope.utente.anagraficaProvinciaNascitaDesc = "";
								$scope.utente.anagraficaProvinciaNascitaEstera = "";
								$scope.utente.anagraficaComuneEsteroDiNascita = "";
								$scope.utente.anagraficaComuneDiNascitaDesc = "";

							};

							// funzione per il select della provincia di nascita
							$scope.selectProvinciaNascita = function($item,
									$model, $label, codStato) {

								if (codStato == 100) {

									$scope.utente.anagraficaProvinciaNascitaDesc = $model.codProvincia;
								} else {
									$scope.utente.anagraficaProvinciaNascitaDesc = $(
											"#provinciaNascita").val();
								}

							};

							// funzione per il select del comune di nascita
							$scope.selectComuneNascita = function($item,
									$model, $label, codStato) {
								console.log(codStato);

								if (codStato == 100) {
									$scope.utente.anagraficaComuneDiNascitaDesc = $model.codComune;
								} else {
									console.log('sono nel comune non italiano');
									$scope.utente.anagraficaComuneDiNascitaDesc = $(
											"#comuneNascita").val();
								}
							};

							// funzione che ritorna l'elenco degli assistenti
							// sociali in base a una UOT
							$scope.elencoAssistenti = function(val) {
								console.log('sono in elenco assistenti' + val);
								return $http
										.get(
												'/CartellaSociale/AssistentiSociali',
												{
													params : {
														codUot : val
													}
												})
										.then(
												function(response) {
													$scope.assistentiSociali = response.data.rows;

												});

							};

							// monitoro il campo uot per caricare l'elenco degli
							// assistenti sociali relativi a quella uot quando
							// quella cambia
							$scope.$watch('utente.anagraficaUot', function(
									newValue, oldValue) {

								$scope.elencoAssistenti(newValue);
							});

							//console.log($scope.elencoUot.Object[0])

						} ]);
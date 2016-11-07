(function() {
    'use strict';

    angular
        .module('project8App')
        .controller('CophongController', CophongController);

    CophongController.$inject = ['$scope', '$state', 'Cophong', 'CophongSearch'];

    function CophongController ($scope, $state, Cophong, CophongSearch) {
        var vm = this;
        
        vm.cophongs = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Cophong.query(function(result) {
                vm.cophongs = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CophongSearch.query({query: vm.searchQuery}, function(result) {
                vm.cophongs = result;
            });
        }    }
})();

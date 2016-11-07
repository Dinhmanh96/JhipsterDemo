(function() {
    'use strict';

    angular
        .module('project8App')
        .controller('CophongDetailController', CophongDetailController);

    CophongDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cophong'];

    function CophongDetailController($scope, $rootScope, $stateParams, previousState, entity, Cophong) {
        var vm = this;

        vm.cophong = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('project8App:cophongUpdate', function(event, result) {
            vm.cophong = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

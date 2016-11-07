(function() {
    'use strict';

    angular
        .module('project8App')
        .controller('CophongDeleteController',CophongDeleteController);

    CophongDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cophong'];

    function CophongDeleteController($uibModalInstance, entity, Cophong) {
        var vm = this;

        vm.cophong = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cophong.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

(function() {
    'use strict';

    angular
        .module('project8App')
        .controller('CophongDialogController', CophongDialogController);

    CophongDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cophong'];

    function CophongDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cophong) {
        var vm = this;

        vm.cophong = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cophong.id !== null) {
                Cophong.update(vm.cophong, onSaveSuccess, onSaveError);
            } else {
                Cophong.save(vm.cophong, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('project8App:cophongUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

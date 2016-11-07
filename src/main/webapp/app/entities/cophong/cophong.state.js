(function() {
    'use strict';

    angular
        .module('project8App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cophong', {
            parent: 'entity',
            url: '/cophong',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'project8App.cophong.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cophong/cophongs.html',
                    controller: 'CophongController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cophong');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cophong-detail', {
            parent: 'entity',
            url: '/cophong/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'project8App.cophong.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cophong/cophong-detail.html',
                    controller: 'CophongDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cophong');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cophong', function($stateParams, Cophong) {
                    return Cophong.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cophong',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cophong-detail.edit', {
            parent: 'cophong-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cophong/cophong-dialog.html',
                    controller: 'CophongDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cophong', function(Cophong) {
                            return Cophong.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cophong.new', {
            parent: 'cophong',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cophong/cophong-dialog.html',
                    controller: 'CophongDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                age: null,
                                phone: null,
                                address: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cophong', null, { reload: 'cophong' });
                }, function() {
                    $state.go('cophong');
                });
            }]
        })
        .state('cophong.edit', {
            parent: 'cophong',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cophong/cophong-dialog.html',
                    controller: 'CophongDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cophong', function(Cophong) {
                            return Cophong.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cophong', null, { reload: 'cophong' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cophong.delete', {
            parent: 'cophong',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cophong/cophong-delete-dialog.html',
                    controller: 'CophongDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cophong', function(Cophong) {
                            return Cophong.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cophong', null, { reload: 'cophong' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

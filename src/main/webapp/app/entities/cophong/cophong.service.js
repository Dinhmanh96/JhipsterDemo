(function() {
    'use strict';
    angular
        .module('project8App')
        .factory('Cophong', Cophong);

    Cophong.$inject = ['$resource'];

    function Cophong ($resource) {
        var resourceUrl =  'api/cophongs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

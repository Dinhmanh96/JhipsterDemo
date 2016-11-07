(function() {
    'use strict';

    angular
        .module('project8App')
        .factory('CophongSearch', CophongSearch);

    CophongSearch.$inject = ['$resource'];

    function CophongSearch($resource) {
        var resourceUrl =  'api/_search/cophongs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

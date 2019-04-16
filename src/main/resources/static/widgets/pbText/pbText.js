(function () {
  try {
    return angular.module('bonitasoft.ui.widgets');
  } catch(e) {
    return angular.module('bonitasoft.ui.widgets', []);
  }
})().directive('pbText', function() {
    return {
      template: '<div\n ng-if="!properties.labelHidden"\n ng-class="{\n    \'form-horizontal\': properties.labelPosition === \'left\' ,\n    \'row\': properties.labelPosition === \'top\'\n    }">\n    <div class="form-group">\n        <label\n            class="control-label col-xs-{{ !properties.labelHidden ? properties.labelWidth : 12 }}" ng-bind-html="properties.label | uiTranslate">\n        </label>\n		<p ng-if="properties.allowHTML" class="form-control-static col-xs-{{ 12 - (!properties.labelHidden && properties.labelPosition === \'left\' ? properties.labelWidth : 0) }}" ng-bind-html="properties.text | uiTranslate"></p>\n		<p ng-if="!properties.allowHTML" class="form-control-static col-xs-{{ 12 - (!properties.labelHidden && properties.labelPosition === \'left\' ? properties.labelWidth : 0) }}" ng-bind="properties.text | uiTranslate"></p>\n    </div>\n</div>\n<p ng-if="properties.labelHidden && properties.allowHTML" class="text-{{ properties.alignment }}" ng-bind-html="properties.text | uiTranslate"></p>\n<p ng-if="properties.labelHidden && !properties.allowHTML" class="text-{{ properties.alignment }}" ng-bind="properties.text | uiTranslate"></p>\n'
    };
  });

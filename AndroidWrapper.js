'use strict';

import React, {
  NativeModules,
  requireNativeComponent,
  PropTypes,
  View
} from "react-native";


var TemasysModule = NativeModules.TemasysModule;
var TemasysView = React.createClass({
  propTypes: {
    preview: PropTypes.bool,
    callId:PropTypes.string,
    ...View.propTypes,
  },

  render: function() {
    return;
  }
});
// var iface = {
//   name: 'VideoView',
//   propTypes: {
//     preview: PropTypes.bool,
//     callId:PropTypes.string,
//     ...View.propTypes,
//   }
// };

var RCTTemasysRendererView = requireNativeComponent('RCTTemasysRendererView', TemasysView);

function TemasysPreview(props) {
  var { style, ...otherProps} = props;
    console.log("TemasysPreview");
    return (
      <View style={style}>
        <RCTTemasysRendererView
          style={{position: 'absolute', top: 0, left: 0, bottom: 0, right: 0}}
          preview={true}
          callId={''} />
      </View>
    );
}

function TemasysRemoteView(props) {
  var { style, callId, ...otherProps} = props;
  if(typeof callId === 'undefined')
    callId = '';
    console.log("TemasysRemoteView");
  return (
      <View style={style}>
        <RCTTemasysRendererView
          style={{position: 'absolute', top: 0, left: 0, bottom: 0, right: 0}}
          preview={false}
          callId={callId} />
      </View>
  );
}


function TemasysSDK () {

  this.connect = function() {
    console.log("TemasysModule.connect");
    TemasysModule.connect();
  };

  this.toggleAudio = function(b) {
    TemasysModule.toggleAudio(b);
  };

  this.toggleVideo = function (doSend) {
    TemasysModule.toggleVideo(doSend);
  };

  this.switchCamera = function() {
    TemasysModule.switchCamera();
  };
}

module.exports = {
    Preview : TemasysPreview,
    RemoteView : TemasysRemoteView,
    SDK : new TemasysSDK()
};

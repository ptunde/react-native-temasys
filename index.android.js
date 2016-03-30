/**
 * Sample React Native App
 */
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



// /**
//  * Sample React Native App
//  * https://github.com/facebook/react-native
//  */
//
// import React, {
//   AppRegistry,
//   Component,
//   StyleSheet,
//   Text,
//   View,
//   DeviceEventEmitter,
// } from 'react-native';
// import AndroidWrapper from "./AndroidWrapper";
//
// class TemasysReact extends Component {
//   componentDidMount() {
//     console.log("TemasysReact");
//     AndroidWrapper.SDK.connect();
//   }
//
//   render() {
//     return (
//       <View style={styles.videopanel}>
//           <AndroidWrapper.RemoteView style={styles.remotevideo}>
//           </AndroidWrapper.RemoteView>
//           <AndroidWrapper.Preview style={styles.selfview}>
//           </AndroidWrapper.Preview>
//       </View>
//     );
//   }
// }
//
// const styles = StyleSheet.create({
//   container: {
//     flex: 1,
//     justifyContent: 'center',
//     alignItems: 'center',
//     backgroundColor: '#F5FCFF',
//   },
//   welcome: {
//     fontSize: 20,
//     textAlign: 'center',
//     margin: 10,
//   },
//   instructions: {
//     textAlign: 'center',
//     color: '#333333',
//     marginBottom: 5,
//   },
//   videopanel: {
//     marginTop: 10,
//     marginBottom: 20
//   },
//   selfview: {
//     position: 'relative',
//     marginTop: -80,
//     left: 110,
//     width: 80,
//     height: 60,
//     borderColor: '#007AFF',
//     borderWidth: 1,
//     alignSelf: 'center',
//     backgroundColor: '#000000'
//   },
//   remotevideo: {
//     width: 320,
//     height: 240,
//     borderColor: '#FF1300',
//     borderWidth: 1,
//     alignSelf: 'center',
//     backgroundColor: '#000000'
//   },
// });
//
// AppRegistry.registerComponent('TemasysReact', () => TemasysReact);

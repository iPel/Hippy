import React from 'react';
import {
  ScrollView,
  Text,
  View,
  StyleSheet,
  Image, Platform,
} from '@hippy/react';

const imgURL = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGAAAAAtCAMAAABmgJ64AAAAOVBMVEX/Rx8AAAD/QiL/Tif/QyH/RR//QiH/QiP/RCD/QSL/Qxz/QyH/QiL/QiD/QyL/QiL/QiH/QyH/QiLwirLUAAAAEnRSTlMZAF4OTC7DrWzjI4iietrRk0EEv/0YAAAB0UlEQVRYw72Y0Y6sIAxAKwUFlFH7/x97izNXF2lN1pU5D800jD2hJAJCdwYZuAUyVbmToKh903IhQHgErAVH+ccV0KI+G2oBPMxJgPA4WAigAT8F0IRDgNAE3ARyfeMFDGSc3YHVFkTBAHKDAgkEyHjacae/GTjxFqAo8NbakXrL9DRy9B+BCQwRcXR9OBKmEuAmAFFgcy0agBnIc1xZsMPOI5loAoUsQFmQjDEL9YbpaeGYBMGRKKAuqFEFL/JXApCw/zFEZk9qgbLGBx0gXLISxT25IUBREEgh1II1fph/IViGnZnCcDDVAgfgVg6gCy6ZaClySbDQpAl04vCGaB4+xGcFRK8CLvW0IBb5bQGqAlNwU4C6oEIVTLTcmoEr0AWcpKsZ/H0NAtkLQffnFjkOqiC/TTWBL9AFCwXQBHgI7rXImMgjCZwFa50s6DRBXyALmIECuMASiWNPFgRTgSJwM+XW8PDCmbwndzdaNL8FMYXPNjASDVChnIvWlBI/MKadPV952HszbmXtRERhhQ0vGFA52SVSSVt7MjHvxfRK8cdTpqovn02dUcltMrwiKf+wQ1FxXKCk9en6e/eDNnP44h2thQEb35O/etNv/q3iHza+KuhqqhZAAAAAAElFTkSuQmCC';
const imgURL2 = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAMAAADXqc3KAAAANlBMVEUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAC3dmhyAAAAEXRSTlMA9QlZEMPc2Mmmj2VkLEJ4Rsx+pEgAAAChSURBVCjPjVLtEsMgCDOAdbbaNu//sttVPes+zvGD8wgQCLp/TORbUGMAQtQ3UBeSAMlF7/GV9Cmb5eTJ9R7H1t4bOqLE3rN2UCvvwpLfarhILfDjJL6WRKaXfzxc84nxAgLzCGSGiwKwsZUB8hPorZwUV1s1cnGKw+yAOrnI+7hatNIybl9Q3OkBfzopCw6SmDVJJiJ+yD451OS0/TNM7QnuAAbvCG0TSAAAAABJRU5ErkJggg==';
const imgURL4 = 'https://user-images.githubusercontent.com/12878546/148736255-7193f89e-9caf-49c0-86b0-548209506bd6.gif';

const styles = StyleSheet.create({
  itemTitle: {
    alignItems: 'flex-start',
    justifyContent: 'center',
    height: 40,
    borderWidth: 1,
    borderColor: '#e0e0e0',
    borderRadius: 2,
    backgroundColor: '#fafafa',
    padding: 10,
    marginTop: 10,
  },
  itemContent: {
    alignItems: 'flex-start',
    justifyContent: 'center',
    borderWidth: 1,
    borderRadius: 2,
    borderColor: '#e0e0e0',
    backgroundColor: '#ffffff',
    padding: 10,
  },
  normalText: {
    fontSize: 14,
    lineHeight: 18,
    color: 'black',
  },
  button: {
    width: 100,
    height: 24,
    borderColor: 'blue',
    borderWidth: 1,
  },
  buttonText: {
    width: 100,
    lineHeight: 24,
    textAlign: 'center',
  },
  customFont: {
    color: '#0052d9',
    fontSize: 32,
    fontFamily: 'TTTGB',
  },
});
let i = 0;
export default class TextExpo extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      fontSize: 16,
      textShadowColor: 'grey',
      textShadowOffset: {
        x: 1,
        y: 1,
      },
    };
    this.incrementFontSize = this.incrementFontSize.bind(this);
    this.decrementFontSize = this.decrementFontSize.bind(this);
    // if Android text nested is used，height and lineHeight attributes should be set in Text wrapper
    this.androidNestedTextWrapperStyle = { height: 100, lineHeight: 100 };
  }

  incrementFontSize() {
    const { fontSize } = this.state;
    if (fontSize === 24) {
      return;
    }
    this.setState({
      fontSize: fontSize + 1,
    });
  }

  decrementFontSize() {
    const { fontSize } = this.state;
    if (fontSize === 6) {
      return;
    }
    this.setState({
      fontSize: fontSize - 1,
    });
  }

  render() {
    const { fontSize, textShadowColor, textShadowOffset } = this.state;
    const renderTitle = title => (
      <View style={styles.itemTitle}>
        <Text style>{title}</Text>
      </View>
    );
    return (
      <ScrollView style={{ padding: 10 }}>
        {renderTitle('shadow')}
        <View style={[styles.itemContent]} onClick={() => {
          let textShadowColor = 'red';
          let textShadowOffset = { x: 10, y: 1 };
          if (i % 2 === 1) {
            textShadowColor = 'grey';
            textShadowOffset = { x: 1, y: 1 };
          }
          i += 1;
          this.setState({
            textShadowColor,
            textShadowOffset,
          });
        }}>
          <Text style={[styles.normalText,
            { color: '#242424',
              textShadowOffset,
              // support declare textShadowOffsetX & textShadowOffsetY separately
              // textShadowOffsetX: 1,
              // textShadowOffsetY: 1,
              textShadowRadius: 3,
              textShadowColor,
            }]}>Text shadow is grey with radius 3 and offset 1</Text>
        </View>
        {renderTitle('color')}
        <View style={[styles.itemContent]}>
          <Text style={[styles.normalText, { color: '#242424' }]}>Text color is black</Text>
          <Text style={[styles.normalText, { color: 'blue' }]}>Text color is blue</Text>
          <Text style={[styles.normalText, { color: 'rgb(228,61,36)' }]}>This is red</Text>
        </View>
        {renderTitle('fontSize')}
        <View style={styles.itemContent}>
          <Text style={[styles.normalText, { fontSize }]}>
            { `Text fontSize is ${fontSize}` }
          </Text>
          <View style={styles.button} onClick={this.incrementFontSize}>
            <Text style={styles.buttonText}>放大字体</Text>
          </View>
          <View style={styles.button} onClick={this.decrementFontSize}>
            <Text style={styles.buttonText}>缩小字体</Text>
          </View>
        </View>
        {renderTitle('fontStyle')}
        <View style={styles.itemContent}>
          <Text style={[styles.normalText, { fontStyle: 'normal' }]}>Text fontStyle is normal</Text>
          <Text style={[styles.normalText, { fontStyle: 'italic' }]}>Text fontStyle is italic</Text>
        </View>
        {renderTitle('numberOfLines')}
        <View style={styles.itemContent}>
          <Text numberOfLines={1} style={styles.normalText}>
            just one line just one line just one line just
            one line just one line just one line just one line just one line
          </Text>
          <Text numberOfLines={2} style={styles.normalText}>
            just two lines just two lines just two lines just
            two lines just two lines
            just two lines just two lines just two lines just two lines just two lines just two
            lines just two lines just two lines just two lines just two lines just two lines
          </Text>
        </View>
        {renderTitle('textDecoration')}
        <View style={styles.itemContent}>
          <Text numberOfLines={1} style={[styles.normalText, { textDecorationLine: 'underline', textDecorationStyle: 'dotted' }]}>
            underline
          </Text>
          <Text numberOfLines={1} style={[styles.normalText, { textDecorationLine: 'line-through', textDecorationColor: 'red' }]}>
            line-through
          </Text>
        </View>
        {renderTitle('Nest Text')}
        <View style={styles.itemContent}>
          <Text numberOfLines={3}>
            <Text numberOfLines={3} style={[styles.normalText, { color: '#4c9afa' }]}>#SpiderMan#</Text>
            <Text numberOfLines={3} style={styles.normalText}>
              Hello world, I am a spider man and I have five friends in other universe.
            </Text>
          </Text>
        </View>
        {renderTitle('Custom font')}
        <View style={styles.itemContent}>
          <Text numberOfLines={1} style={styles.customFont}>Hippy 跨端框架</Text>
        </View>
        {renderTitle('Text Nested')}
        <View style={styles.itemContent}>
          <Text style={Platform.OS === 'android' ? this.androidNestedTextWrapperStyle : {}}>
            <Text numberOfLines={1} style={styles.normalText}>后面有张图片</Text>
            <Image style={{ width: 70, height: 35 }} source={{ uri: imgURL }} />
            <Text numberOfLines={1} style={styles.customFont}>前面有张图片</Text>
          </Text>
        </View>
        {renderTitle('verticalAlign')}
        <View style={[styles.itemContent, { height: Platform.OS === 'android' ? 160 : 70 }]}>
          <Text style={[styles.normalText,
            { lineHeight: 50, backgroundColor: '#4c9afa', paddingHorizontal: 10, paddingVertical: 5 }]}>
            <Image style={{ width: 24, height: 24, verticalAlign: 'top' }} source={{ uri: imgURL2 }} />
            <Image style={{ width: 18, height: 12, verticalAlign: 'middle' }} source={{ uri: imgURL2 }} />
            <Image style={{ width: 24, height: 12, verticalAlign: 'baseline' }} source={{ uri: imgURL2 }} />
            <Image style={{ width: 36, height: 24, verticalAlign: 'bottom' }} source={{ uri: imgURL2 }} />
            <Image style={{ width: 24, height: 24, verticalAlign: 'top' }} source={{ uri: imgURL4 }} />
            <Image style={{ width: 18, height: 12, verticalAlign: 'middle' }} source={{ uri: imgURL4 }} />
            <Image style={{ width: 24, height: 12, verticalAlign: 'baseline' }} source={{ uri: imgURL4 }} />
            <Image style={{ width: 36, height: 24, verticalAlign: 'bottom' }} source={{ uri: imgURL4 }} />
            <Text style={{ fontSize: 16, verticalAlign: 'top' }}>字</Text>
            <Text style={{ fontSize: 16, verticalAlign: 'middle' }}>字</Text>
            <Text style={{ fontSize: 16, verticalAlign: 'baseline' }}>字</Text>
            <Text style={{ fontSize: 16, verticalAlign: 'bottom' }}>字</Text>
          </Text>
          {Platform.OS === 'android' && (<>
            <Text>legacy mode:</Text>
            <Text style={[styles.normalText,
              { lineHeight: 50, backgroundColor: '#4c9afa', marginBottom: 10, paddingHorizontal: 10, paddingVertical: 5 }]}>
              <Image style={{ width: 24, height: 24, verticalAlignment: 0 }} source={{ uri: imgURL2 }} />
              <Image style={{ width: 18, height: 12, verticalAlignment: 1 }} source={{ uri: imgURL2 }} />
              <Image style={{ width: 24, height: 12, verticalAlignment: 2 }} source={{ uri: imgURL2 }} />
              <Image style={{ width: 36, height: 24, verticalAlignment: 3 }} source={{ uri: imgURL2 }} />
              <Image style={{ width: 24, height: 24, top: -10 }} source={{ uri: imgURL4 }} />
              <Image style={{ width: 18, height: 12, top: -5 }} source={{ uri: imgURL4 }} />
              <Image style={{ width: 24, height: 12 }} source={{ uri: imgURL4 }} />
              <Image style={{ width: 36, height: 24, top: 3 }} source={{ uri: imgURL4 }} />
              <Text style={{ fontSize: 16 }}>字</Text>
              <Text style={{ fontSize: 16 }}>字</Text>
              <Text style={{ fontSize: 16 }}>字</Text>
              <Text style={{ fontSize: 16 }}>字</Text>
            </Text>
          </>)}
        </View>
        {renderTitle('tintColor & backgroundColor')}
        <View style={[styles.itemContent]}>
          <Text style={[styles.normalText,
            { lineHeight: 30, backgroundColor: '#4c9afa', paddingHorizontal: 10, paddingVertical: 5 }]}>
            <Image style={{ width: 24, height: 24, verticalAlign: 'middle', tintColor: 'orange' }} source={{ uri: imgURL2 }} />
            <Image style={{ width: 24, height: 24, verticalAlign: 'middle', tintColor: 'orange', backgroundColor: '#ccc' }} source={{ uri: imgURL2 }} />
            <Image style={{ width: 24, height: 24, verticalAlign: 'middle', backgroundColor: '#ccc' }} source={{ uri: imgURL2 }} />
            <Text style={{ verticalAlign: 'middle', backgroundColor: '#090' }}>text</Text>
          </Text>
          {Platform.OS === 'android' && (<>
            <Text>legacy mode:</Text>
            <Text style={[styles.normalText,
              { lineHeight: 30, backgroundColor: '#4c9afa', marginBottom: 10, paddingHorizontal: 10, paddingVertical: 5 }]}>
              <Image style={{ width: 24, height: 24, tintColor: 'orange' }} source={{ uri: imgURL2 }} />
              <Image style={{ width: 24, height: 24, tintColor: 'orange', backgroundColor: '#ccc' }} source={{ uri: imgURL2 }} />
              <Image style={{ width: 24, height: 24, backgroundColor: '#ccc' }} source={{ uri: imgURL2 }} />
            </Text>
          </>)}
        </View>
        {renderTitle('margin')}
        <View style={[styles.itemContent]}>
          <Text style={[
            { lineHeight: 50, backgroundColor: '#4c9afa', marginBottom: 5 }]}>
            <Image style={{ width: 24, height: 24, verticalAlign: 'top', backgroundColor: '#ccc', margin: 5 }} source={{ uri: imgURL2 }} />
            <Image style={{ width: 24, height: 24, verticalAlign: 'middle', backgroundColor: '#ccc', margin: 5 }} source={{ uri: imgURL2 }} />
            <Image style={{ width: 24, height: 24, verticalAlign: 'baseline', backgroundColor: '#ccc', margin: 5 }} source={{ uri: imgURL2 }} />
            <Image style={{ width: 24, height: 24, verticalAlign: 'bottom', backgroundColor: '#ccc', margin: 5 }} source={{ uri: imgURL2 }} />
          </Text>
          {Platform.OS === 'android' && (<>
            <Text>legacy mode:</Text>
            <Text style={[styles.normalText,
              { lineHeight: 50, backgroundColor: '#4c9afa', marginBottom: 10, paddingHorizontal: 10, paddingVertical: 5 }]}>
              <Image style={{ width: 24, height: 24, verticalAlignment: 0, backgroundColor: '#ccc', margin: 5 }} source={{ uri: imgURL2 }} />
              <Image style={{ width: 24, height: 24, verticalAlignment: 1, backgroundColor: '#ccc', margin: 5 }} source={{ uri: imgURL2 }} />
              <Image style={{ width: 24, height: 24, verticalAlignment: 2, backgroundColor: '#ccc', margin: 5 }} source={{ uri: imgURL2 }} />
              <Image style={{ width: 24, height: 24, verticalAlignment: 3, backgroundColor: '#ccc', margin: 5 }} source={{ uri: imgURL2 }} />
            </Text>
          </>)}
        </View>
      </ScrollView>
    );
  }
}

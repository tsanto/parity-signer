import React, { Component, PropTypes } from 'react'
import { Text, StyleSheet, ListView } from 'react-native'
import AccountsListRow from './AccountsListRow'

export default class AccountsList extends Component {
  static propTypes = {
    accounts: PropTypes.arrayOf(PropTypes.string).isRequired,
  }

  constructor(props) {
    super(props)
    const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2})
    this.state = {
      dataSource: ds.cloneWithRows(props.accounts)
    }
  }

  componentWillReceiveProps(nextProps) {
    this.setState({
      dataSource: this.state.dataSource.cloneWithRows(nextProps.accounts)
    })
  }

  render() {
    return (
      <ListView
        style={styles.view}
        dataSource={this.state.dataSource}
        renderRow={(rowData) => <AccountsListRow text={rowData}/>}
        enableEmptySections={true}
      />
    )
  }
}

const styles = StyleSheet.create({
  view: {
    flex: 1,
    marginTop: 60,
    marginBottom: 50,
  },
})


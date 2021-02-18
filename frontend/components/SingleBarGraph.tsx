import * as React from 'react';
import Paper from '@material-ui/core/Paper';
import {
  Chart,
  BarSeries,
  Title,
  ArgumentAxis,
  ValueAxis,
} from '@devexpress/dx-react-chart-material-ui';

import { Animation } from '@devexpress/dx-react-chart';

const data = [
  { date: 'Jan 10', totalCount: 3 },
  { date: 'Jan 15', totalCount: 20 },
  { date: 'Jan 20', totalCount: 5 },
  { date: 'Jan 25', totalCount: 8 },
  { date: 'Jan 30', totalCount: 7 },
  { date: 'Feb 5', totalCount: 12 },
  { date: 'Feb 15', totalCount: 25 },
];

export default class Demo extends React.PureComponent {
  constructor(props) {
    super(props);

    this.state = {
      data,
    };
  }

  render() {
    const { data: chartData } = this.state;

    return (
      <Paper>
        <Chart
          data={chartData}
        >
          <ArgumentAxis />
          <ValueAxis max={10} />

          <BarSeries
            valueField="Total Count"
            argumentField="Date"
          />
          <Title text="Daily Total of Commmit/Merge Requests by Everyone" />
          <Animation />
        </Chart>
      </Paper>
    );
  }
}
import * as React from 'react';
import Paper from '@material-ui/core/Paper';
import { Chart, BarSeries, Title, ArgumentAxis, ValueAxis } from '@devexpress/dx-react-chart-material-ui';
import { Animation } from '@devexpress/dx-react-chart';

// From: https://devexpress.github.io/devextreme-reactive/react/chart/demos/bar/simple-bar/
// TODO: replace data with real data, add x and y axis titles

const data = [
  { date: 'Jan 10', totalCount: 2 },
  { date: 'Jan 15', totalCount: 4 },
  { date: 'Jan 20', totalCount: 20 },
  { date: 'Jan 25', totalCount: 5 },
  { date: 'Jan 30', totalCount: 17 },
  { date: 'Feb 5', totalCount: 9 },
  { date: 'Feb 10', totalCount: 9 },
  { date: 'Feb 15', totalCount: 18 },
];

export default class SingleBarGraph extends React.PureComponent {
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
          <ValueAxis max={7} />

          <BarSeries
            valueField="totalCount"
            argumentField="date"
          />
          <Title text="Daily Total of {commit or merge} Requests by Everyone" />
          <Animation />
        </Chart>
      </Paper>
    );
  }
}
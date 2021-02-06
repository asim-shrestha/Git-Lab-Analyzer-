import { Box, Button, TextField, Typography } from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";
import 'date-fns';
import Grid from '@material-ui/core/Grid';
import DateFnsUtils from '@date-io/date-fns';
import {
  MuiPickersUtilsProvider,
  KeyboardDatePicker,
} from '@material-ui/pickers';
import React, {useState} from "react";
import Image from "next/image";

const useStyles = makeStyles({
  backgroundGradient: {
    background: "radial-gradient(90% 180% at 50% 50%, #FFFFFF 0%, #FCA326 65%)",
  },
  card: {
    background: "white",
  },
});



const index = () => {
    const classes = useStyles();
    const [startDate, setStartDate] = React.useState<Date | null>(new Date('01-02-2021'));
    const [endDate, setEndDate] =  React.useState<Date | null>(new Date('01-05-2021'));

    const handleStartChange = (date: Date | null) => {
        setStartDate(date);
    };

    const handleEndChange = (date: Date | null) => {
        setEndDate(date);
      };

    return (
        <Box
            className={classes.backgroundGradient}
            height="100vh"
            width="100vw"
            display="flex"
            justifyContent="center"
            alignItems="center"
        >
            <Box
                className={classes.card}
                boxShadow={20}
                width="70vw"
                height="70vh"
                minWidth="60vw"
                minHeight="60vw"
                display="flex"
                flexDirection="column"
                justifyContent="center"
                alignItems="center"
                borderRadius={42}
                padding="25px"
            >
                <Image
                    src="/gitlab.svg"
                    alt="The Gitlab Logo"
                    width={100}
                    height={100}
                />
                <Typography variant="h6" align="center">
                    GitLab
                    <br />
                    Analyzer
                </Typography>

                <Box
                    className={classes.card}
                    boxShadow={20}
                    width="70vw"
                    height="10vh"
                    minWidth="60vw"
                    minHeight="10vh"
                    display="flex"
                    flexDirection="column"
                    justifyContent="column"
                    alignItems="column"
                >
                    <div className='Date picker'>
                        <MuiPickersUtilsProvider utils={DateFnsUtils}>
                            <Grid container justify="space-around">
                                <KeyboardDatePicker
                                    disableToolbar
                                    variant="inline"
                                    format="dd/MM/yyyy"
                                    margin="normal"
                                    id="start-date-picker"
                                    label="Start Date:"
                                    value={startDate}
                                    onChange={handleStartChange}
                                    autoOk={true}
                                    KeyboardButtonProps={{
                                    'aria-label': 'change date',
                                    }}
                                
                                />
                        
                                <KeyboardDatePicker
                                    disableToolbar
                                    variant="inline"
                                    format="dd/MM/yyyy"
                                    margin="normal"
                                    id="end-date-picker"
                                    label="End Date:"
                                    value={endDate}
                                    onChange={handleEndChange}
                                    autoOk={true}
                                    KeyboardButtonProps={{
                                        'aria-label': 'change date',
                                    }}
                                />
                            </Grid>
                        </MuiPickersUtilsProvider>
                    </ div>
                </Box>

            </Box>

        </Box>
    );
};
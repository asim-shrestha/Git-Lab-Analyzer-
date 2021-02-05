import { Box, Button, TextField, Typography } from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";
import DatePicker from "react-datepicker"
import 'react-datepicker/dist/react-datepicker.css'
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
    const [startDate, setStartDate] = useState(new Date());
    const [endDate, setEndDate] = useState(new Date());
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
                    <div className='StartDatePicker'>
                        <DatePicker
                            selected={startDate}
                            onChange={date => setStartDate(date)}
                            dateFormat = 'dd/MM/yyyy'
                            minDate= {new Date("01-02-2021")}
                            scrollableMonthYearDropdown
                            placeholderText="Start date"
                            
                        />
                    </ div>

                    <div className='EndDatePicker'>
                        <DatePicker
                            selected = {endDate}
                            onChange = {date => date && setEndDate(date)}
                            dateFormat = 'dd/MM/yyyy'
                            maxDate= {new Date("01-06-2021")}
                            scrollableMonthYearDropdown
                            placeholderText="End Date:"
                            
                        />
                    </ div>

                </Box>

            </Box>

        </Box>
    );
};
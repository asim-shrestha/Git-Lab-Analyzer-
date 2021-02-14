import { Box, Button, TextField, Typography, LinearProgress } from "@material-ui/core";
import { makeStyles } from "@material-ui/core/styles";
import 'date-fns';
import Grid from '@material-ui/core/Grid';
import DateFnsUtils from '@date-io/date-fns';
import { MuiPickersUtilsProvider, KeyboardDatePicker} from '@material-ui/pickers';
import React, {useState, useEffect} from "react";
import Image from "next/image";
import {useRouter} from "next/router";
import axios, {AxiosResponse} from "axios";
import CardLayout from "../../components/CardLayout";
import {GitLabProject} from "../../interfaces/GitLabProject";
import Autocomplete from "@material-ui/lab/Autocomplete";


const useStyles = makeStyles({
  card: {
    background: "white",
  },
});

const LoadingBar = () => {
  return <div>
    <Typography variant={"body1"}>
      Loading projects...
    </Typography>
    <LinearProgress />
  </div>;
}


const index = () => {
    const classes = useStyles();
    const [startDate, setStartDate] = React.useState<Date | null>(new Date('01-02-2021'));
    const [endDate, setEndDate] =  React.useState<Date | null>(new Date('01-05-2021'));
    const [projects, setProjects] = useState<GitLabProject[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const router = useRouter();
    const { serverId } =  router.query;


    const handleStartChange = (date: Date | null) => {
        setStartDate(date);
    };

    const handleEndChange = (date: Date | null) => {
        setEndDate(date);
    };

    useEffect(() => {
    if (router.isReady) {
      // TODO need to pass serverId into this call to get the correct gitlab url and access code from db
      // when that information is available in db
      axios
          .get(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects`)
          .then((resp: AxiosResponse) => {
            setProjects(resp.data);
            setIsLoading(false);
          });
    }
    }, [serverId]);

    let loadingBar =  null;
    if (isLoading) {
        loadingBar = <LoadingBar />;
    }



    return (
        <CardLayout>
            {loadingBar}
            {!isLoading && <Autocomplete
                id="project-select"
                options={projects}
                getOptionLabel={(proj) => proj.name_with_namespace}
                renderInput={(params) => <TextField {...params} label="Search Projects" variant="outlined" />}
            /> }

            <Box
                    className={classes.card}
                    boxShadow={20}
                    width="60vw"
                    height="10vh"
                    minWidth="250px"
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
        </CardLayout>

    );
};
        export default index;
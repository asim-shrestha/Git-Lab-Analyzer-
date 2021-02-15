import React from "react";

import { createMuiTheme, createStyles, withStyles, makeStyles, Theme, ThemeProvider, } from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import { Box, Icon } from "@material-ui/core";

const BootstrapButton = withStyles({
  root: {
    boxShadow: 'none',
    textTransform: 'none',
    fontSize: 16,
    padding: '20px',
    border: '1px solid',
    color: 'black',
    width: '80%',
    lineHeight: 0.8,
    backgroundColor: 'white',
    borderColor: 'none',
    borderRadius: '999px',
    margin: '20px 0',
    fontFamily: [
      '-apple-system',
      'BlinkMacSystemFont',
      '"Segoe UI"',
      'Roboto',
      '"Helvetica Neue"',
      'Arial',
      'sans-serif',
      '"Apple Color Emoji"',
      '"Segoe UI Emoji"',
      '"Segoe UI Symbol"',
    ].join(','),
    '&:hover': {
      backgroundColor: '#8FC6F3',
      borderColor: '#8FC6F3',
      boxShadow: 'none',
    },
    '&:active': {
      boxShadow: 'none',
      backgroundColor: 'primary',
      borderColor: '#005cbf',
    },
    '&:focus': {
      boxShadow: '0 0 0 0.2rem rgba(0,123,255,.5)',
    },
  },
})(Button);

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
  backgroundGradient: {
     background: "#187BCD",
  },
  card: {
     background: "white",
  },
  button: {
      margin: theme.spacing(1),
      borderRadius: "999"
  },
  sidebar__breakpoint__text: {
    fontSize: '16px',
    color: 'white',
    marginTop: 0,
    marginBottom: 0,
  },
  sidebar__breakpoint: {
     backgroundColor: 'white',
     width: '100%',
     height: '2.5px',
  },
}));


// TODO: need to make an API request (backend should have contributors: id, name, etc),
// populate an array of names, sort names alphabetically and display them using a mapping
// into "sidebar__button"

const MenuSideBar = () => {
  const classes = useStyles();

  return (
  <Box
        className={classes.backgroundGradient}
        height="100vh"
        width="16vw"
        display="flex"
        flexDirection="column"
        justifyContent="flex-start"
        alignItems="center"
        >

        <BootstrapButton variant="contained" color="primary" disableRipple className={classes.margin}>
            Everyone
        </BootstrapButton>

        <p className={classes.sidebar__breakpoint__text}>Contributors</p>
        <div className={classes.sidebar__breakpoint}></div>
        <BootstrapButton variant="contained" color="primary" disableRipple className={classes.margin}>
            memberName
        </BootstrapButton>
      </Box>
  );
};

export default MenuSideBar;
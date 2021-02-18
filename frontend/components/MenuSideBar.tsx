import React from "react";

import {  withStyles, makeStyles } from "@material-ui/core/styles";
import Button from "@material-ui/core/Button";
import { Box } from "@material-ui/core";

const MenuButton = withStyles({
  root: {
    boxShadow: 'none',
    textTransform: 'none',
    fontSize: 16,
    padding: '20px',
    border: '1px solid white',
    color: 'black',
    width: '80%',
    lineHeight: 0.8,
    backgroundColor: 'white',
    borderColor: 'none',
    borderRadius: '999px',
    margin: '20px 0',
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
  background: {
     background: theme.palette.primary.main,
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
// populate an array of names, sort names alphabetically and display them using mapping

const MenuSideBar = () => {
  const classes = useStyles();

  return (
  <Box
        className={classes.background}
        height="100vh"
        width="16vw"
        display="flex"
        flexDirection="column"
        justifyContent="flex-start"
        alignItems="center"
        >

        <MenuButton variant="contained" color="primary" disableRipple className={classes.margin}>
            Everyone
        </MenuButton>
        <p className={classes.sidebar__breakpoint__text}>Contributors</p>
        <div className={classes.sidebar__breakpoint}></div>
        <MenuButton variant="contained" color="primary" disableRipple className={classes.margin}>
            memberName
        </MenuButton>
      </Box>
  );
};

export default MenuSideBar;
import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Typography from "@material-ui/core/Typography";
import Button from "@material-ui/core/Button";
import Image from "next/image";
import SvgIcon from "@material-ui/core/SvgIcon";
import { Box, Icon } from "@material-ui/core";

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
  },
  backgroundGradient: {
     background: "#FCA326",
  },
  card: {
     background: "white",
  },
  button: {
      margin: theme.spacing(1),
      borderRadius: "999"
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

        <button className="sidebar__button sidebar__button--active" onclick="general overview">
            Everyone
        </button>

        <p className="sidebar__breakpoint__text">Contributors</p>
        <div className="sidebar__breakpoint"></div>

        <button className="sidebar__button" >
            memberName
        </button>
      </Box>
  );
};

export default MenuSideBar;
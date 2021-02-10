import React, { useState, useEffect } from "react";
import axios, { AxiosResponse } from "axios";

const BackendTest = () => {
  const [repos, setRepos] = useState([]);

  // Load repo data
  useEffect(() => {
    axios
      .get(`${process.env.NEXT_PUBLIC_API_URL}/projects`)
      .then((resp: AxiosResponse) => {
        setRepos(resp.data);
      });
  }, []);

  return (
    <div>
      <h4>API URL: {process.env.NEXT_PUBLIC_API_URL}</h4>
      <p>API data: {JSON.stringify(repos)}</p>
    </div>
  );
};

export default BackendTest;

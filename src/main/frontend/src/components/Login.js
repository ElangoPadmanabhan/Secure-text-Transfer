import React from 'react'
import GoogleAuth from '../GoogleAuth'
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function Login() {  

    const navigate = useNavigate();
    const GoogleLoginCb = async (googleResp) => {

        if(!googleResp || (googleResp && !googleResp.credential)) {
            console.log('failed');
            return;
        }

        console.log("success - Before backend call");
        console.log(googleResp.credential);

        axios.post("/login", null, {headers: {"authentication" : googleResp.credential}})
        .then((response) => {
            if(response.data.role === 1) {
                navigate("/createUser");
            }
            else {
                navigate("/inbox");
            }
        })
        .catch(e => {
            console.log(e);
            alert("Please try again later");
        });
    };

  return (
    <GoogleAuth GoogleLoginCb = {GoogleLoginCb} />
  )
}

export default Login
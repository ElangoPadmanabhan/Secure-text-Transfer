import { useCallback, useState } from 'react';
import { Box, Card, Button,CardContent, CardHeader, Unstable_Grid2 as Grid, TextField } from '@mui/material';
import axios from 'axios';

import React from 'react'
import { useNavigate } from 'react-router-dom';

function CreateUser() {

    const navigate = useNavigate();

    const [values, setValues] = useState({
        name:"",
        gender:""
    });

        const handleChange = useCallback(
        (event) => {

            const { name, value } = event.target;

            setValues((prevState) => ({
            ...prevState,
            [name]: value
            }));
        },
        [setValues]
        );

        const handleSubmit = useCallback(
        async (event) => {
            event.preventDefault();

            // event.target.elements[REG_FORM_KEY.EMAIL].value

            const formData = new FormData();
            formData.append("name", event.target.elements["name"].value);
            formData.append("gender", event.target.elements["gender"].value);
            formData.append("dob", event.target.elements["dob"].value);
            formData.append("phoneNumber", event.target.elements["phoneNumber"].value);

            axios.post("/createUser", formData,{
                headers:{"Content-Type": "multipart/form-data"}
            })
            .then((response) => {
                console.log(response);
                if(response.data && response.data.name) navigate("/inbox");
                else {
                    alert("Please try again later");
                }
            })
            .catch(e => {
                console.log(e);
            });
        },
        []
        );

        return (
        <form
            onSubmit={handleSubmit}
        >
            <Card sx = {{height:"100%"}}>
            <CardHeader
                subheader = {""}
                title = {"Create User"}
            />
            <CardContent sx={{ p: 5, pb:0 }}>
                <Box sx={{ m: -1.5 }}>
                    <Grid
                        container
                        spacing={6}
                    >
                        <Grid
                            xs={12}
                        >
                            <TextField
                            fullWidth
                            label="Name"
                            name="name"
                            required
                            onChange={handleChange}
                            value={values["name"]}
                            />
                        </Grid>
                        <Grid
                            xs={12}
                        >
                            <TextField
                            fullWidth
                            label="Gender"
                            name="gender"
                            required
                            onChange={handleChange}
                            value={values["gender"]}
                            />
                        </Grid>
                        <Grid
                            xs={12}
                        >
                            <TextField
                            fullWidth
                            label="Date of birth"
                            name="dob"
                            required
                            onChange={handleChange}
                            value={values["dob"]}
                            />
                        </Grid>
                        <Grid
                            xs={12}
                        >
                            <TextField
                            fullWidth
                            label="Phone Number"
                            name="phoneNumber"
                            required
                            onChange={handleChange}
                            value={values["phoneNumber"]}
                            />
                        </Grid>
                    </Grid>
                </Box>
            </CardContent>
        </Card>
        <Box sx={{ mt: 5, textAlign: "center" }}>
            <Button type='submit' variant='contained'>Proceed</Button>
        </Box>
        </form>
        );
}

export default CreateUser

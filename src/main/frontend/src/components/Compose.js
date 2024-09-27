import { useCallback, useState } from 'react';
import { Box, Card, Button,CardContent, CardHeader, Unstable_Grid2 as Grid, TextField } from '@mui/material';

import React from 'react'
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function Compose() {

    const navigate = useNavigate();
    const [values, setValues] = useState({
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

            const formData = new FormData();
            formData.append("receiverEmail", event.target.elements["to"].value);
            formData.append("subject", event.target.elements["subject"].value);
            formData.append("text", event.target.elements["message"].value);

            axios.post("/sendingEmail", formData, {
                headers:{"Content-Type": "multipart/form-data"}
            })
            .then((response) => {
                console.log(response);
                if(response.status === 200) navigate("/inbox");
                else alert("Please try again later");
            })
            .catch(e => {
                console.log(e);
                alert("Please try again later");
            });

            // event.target.elements[REG_FORM_KEY.EMAIL].value
        },
        []
        );

  const Cancel = () => {
    navigate("/inbox");
  };
        return (
        <form
            onSubmit={handleSubmit}
        >
            <Card sx = {{height:"100%"}}>
            <CardHeader
                subheader = {""}
                title = {"Compose"}
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
                            label="To"
                            name="to"
                            placeholder="To"
                            required
                            onChange={handleChange}
                            value={values["to"]}
                            />
                        </Grid>
                        <Grid
                            xs={12}
                        >
                            <TextField
                            fullWidth
                            label="Subject"
                            placeholder="Subject"
                            name="subject"
                            required
                            onChange={handleChange}
                            value={values["subject"]}
                            />
                        </Grid>
                        <Grid
                            xs={12}
                        >
                        <TextField
                            fullWidth
                            placeholder="Message"
                            name="message"
                            multiline
                            required
                            rows={4}
                            maxRows={4}
                            onChange={handleChange}
                            value={values["message"]}
                        />
                        </Grid>
                    </Grid>
                </Box>
            </CardContent>
        </Card>
        <Box sx={{ mt: 5, textAlign: "center" }}>
            <Button type='submit' variant='contained'>Send</Button>
            <Button onClick = {Cancel} variant='contained' sx={{ m : 2 }}>Cancel</Button>
        </Box>
        </form>
        );
}

export default Compose

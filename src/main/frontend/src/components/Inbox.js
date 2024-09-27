import React, { useCallback, useEffect, useMemo, useState } from 'react'
import {
  Avatar,
  Box,
  Button,
  Card,
  CircularProgress,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TablePagination,
  TableRow,
  Typography
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function applyPagination(documents, page, rowsPerPage) {
  return documents.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage);
}

const useCustomers = (data, page, rowsPerPage) => {
  return useMemo(
    () => {
      return applyPagination(data, page, rowsPerPage);
    },
    [data, page, rowsPerPage]
  );
};

const Inbox = ({user}) => {

  const navigate = useNavigate();

    const [loading, setLoading] = useState(true);
  const [data, setData] = useState([]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(100);
  const tableData = useCustomers(data, page, rowsPerPage);

  const handlePageChange = useCallback(
    (event, value) => {
      setPage(value);
    },
    []
  );

  const handleRowsPerPageChange = useCallback(
    (event) => {
      setRowsPerPage(event.target.value);
    },
    []
  );

  const fetchData = useCallback(async () => {

    axios.get("/getMailByUser")
    .then((response) => {
        setLoading(false);
        if(response.status === 200) {
          setData(response.data);
        }
        else {
          alert("Please try again later");
        }
    })
    .catch(e => {
        console.log(e);
        setLoading(false);
        alert("Please try again later");
    });

    /*var resp = [{
      from:"yukesh@gmail.com",
      message:"hi ..."
    },
    {
      from:"rajesh@gmail.com",
      message:"helloo ..."
    }];
    setData(resp);
    */
    
  }, []);

  useEffect(() => {

    fetchData();
    
  }, []);

  const compose = () => {
    navigate("/compose");
  };

  const viewMail = (data) => {
    console.log(data);
  }


  return (
    <>
    <Box sx={{ minWidth: 800, display: "flex", p:5 }}>
              <Button onClick = {compose} variant='contained'>Compose</Button>
            </Box>
      {
        (loading) ?
        <Box sx={{ width: '100%', height: "200px", display: "flex", justifyContent: "center", alignItems: "center" }}>
          <CircularProgress />
        </Box>
        :
        (data.length)?
        <>
        <Card sx={{overflowX:"auto" }}>
          <Box sx={{ minWidth: 800}}>
              <Table>
              <TableHead>
                  <TableRow>
                  <TableCell>
                  From
                </TableCell>
                <TableCell>
                    Subject
                </TableCell>
                <TableCell>
                    Date
                </TableCell>
                <TableCell>
                    Message
                </TableCell>
                  </TableRow>
              </TableHead>
              <TableBody>
                  {tableData.map((data, i) => {

                  return (
                      <TableRow
                      hover
                      key={i}
                      onClick = {(data) => {viewMail(data)}}
                      >
                      <TableCell>
                        {data.senderEmail}
                      </TableCell>
                      <TableCell>
                        {data.subject}
                      </TableCell>
                      <TableCell>
                        {data.date}
                      </TableCell>
                      <TableCell>
                        {data.text}
                      </TableCell>
                      </TableRow>
                  );
                  })}
              </TableBody>
              </Table>
          </Box>
        <TablePagination
          component="div"
          count={data.length}
          onPageChange={handlePageChange}
          onRowsPerPageChange={handleRowsPerPageChange}
          page={page}
          rowsPerPage={rowsPerPage}
          rowsPerPageOptions={[5, 10, 100]}
        />
      </Card>
      </>
        :
        <></>
      }
    </>
  )
}

export default Inbox
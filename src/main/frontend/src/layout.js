import { Box } from '@mui/material';
import LoginBg from './assets/bg.jpg';

export const Layout = (props) => {
  const { children} = props;

  const loginStyle = {

    conatiner:{
      position:"relative",
      width:"100%",
      display:"flex",
      justifyContent:"center",
      alignItems:"flex-start",
      overflow:"hidden",
      borderRadius:0, 
      height:"100vh"
    },
    bgimg:{
      width:"100%",
      height:"100%",
      objectFit:"cover",
      filter:"blur(3px)",
      transform:"scale(1)"
    },
    dullscreen:{
      position: 'absolute',
      top: '0',
      left: '0',
      height: '100%',
      width: '100%',
      zIndex: '5',
      background: 'rgb(0 0 0 / 12%)'
    },
    SignInScreenWrapper:{
      display:"flex",
      justifyContent:"center",
      alignItems:"center",
      position: "absolute",
      bottom:0,
      left:0,
      width:"100%",
      height:"100%",
      zIndex:6,
      flexDirection:"column", 
      textAlign:"center"
    }
  }; 
  

  return (
  <Box sx={loginStyle.conatiner} >
    <img style={loginStyle.bgimg} src = {LoginBg} alt='Sepaktakaraw India registration'/>
    <Box sx={loginStyle.dullscreen} ></Box>
    <Box sx = {loginStyle.SignInScreenWrapper} >
        {children}
    </Box>
  </Box>
  );
};
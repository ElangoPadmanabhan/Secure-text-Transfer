/* 
  Google login button component
*/
import { useEffect, useRef } from 'react';

const loadScript = (src) =>
new Promise((resolve, reject) => {
  if (document.querySelector(`script[src="${src}"]`)) return resolve()
  const script = document.createElement('script');
  script.src = src;
  script.onload = () => resolve();
  script.onerror = (err) => reject(err);
  document.body.appendChild(script);
});

const GoogleAuth = ({GoogleLoginCb, auto_select, login_hint}) => {

  const GOOGLE_CLIENT_ID = '581714176969-i6jq47vjml7g0mu7p7pehtjnt8qj8ds6.apps.googleusercontent.com';
  const googleButton = useRef(null);

  useEffect(() => {
    const src = 'https://accounts.google.com/gsi/client';

    loadScript(src)
      .then(() => {
        const google = window.google;
        
        google.accounts.id.initialize({
          client_id: GOOGLE_CLIENT_ID,
          callback: GoogleLoginCb,
          auto_select: auto_select,
          login_hint: login_hint
        });

        google.accounts.id.renderButton(
          googleButton.current, 
          { theme: 'outline', size: 'large',shape: 'circle'} 
        );
        // google.accounts.id.disableAutoSelect()
        google.accounts.id.prompt();
      })
      .catch(console.error);
      

    return () => {
      const scriptTag = document.querySelector(`script[src="${src}"]`)
      if (scriptTag) document.body.removeChild(scriptTag)
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <div className='googlebutton' ref={googleButton}></div>
  );
};

export default GoogleAuth;
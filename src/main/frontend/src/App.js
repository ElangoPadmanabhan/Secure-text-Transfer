import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import './App.css';
import Login from './components/Login';
import { Layout } from './layout';
import CreateUser from './components/CreateUser';
import Inbox from './components/Inbox';
import Compose from './components/Compose';

function App() {

  const router = createBrowserRouter([
    {
      path: "/",
      Component: Login,
    },
    {
      path: "/createUser",
      Component: CreateUser,
    },
    {
      path: "/inbox",
      Component: Inbox,
    },
    {
      path: "/compose",
      Component: Compose,
    }
  ]);

  return (
    <Layout>
      <RouterProvider router={router} />
    </Layout>
  );
}

export default App;

import { Button, Center, Spinner, Text } from '@chakra-ui/react';
import { useEffect, useState } from 'react';
import SidebarWithHeader from './components/shared/Sidebar';
import { getCustomers } from './services/client';
import CardWithImage from './components/Card';
import { Wrap, WrapItem } from '@chakra-ui/react'

const App = () => {
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    setTimeout(() => {
      getCustomers()
      .then((res) => {
        setCustomers(res.data);
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => {
        setLoading(false);
      });
    },1000)
  }, []);

  if (loading) {
    return (
      <SidebarWithHeader>
        <Spinner
          thickness="4px"
          speed="0.65s"
          emptyColor="gray.200"
          color="blue.500"
          size="xl"
        />
      </SidebarWithHeader>
    );
  }

  if (customers.length <= 0) {
    return (
      <SidebarWithHeader>
        <Text>No customers available</Text>
      </SidebarWithHeader>
    );
  }

  return (
    <SidebarWithHeader>
      <Wrap spacing='30px' justify='center'>
      {customers.map((customer, index) => (
        <WrapItem key={index}>
          <CardWithImage {...customer}/>
        </WrapItem>
      ))}
      </Wrap>
     
    </SidebarWithHeader>
  );
};

export default App;

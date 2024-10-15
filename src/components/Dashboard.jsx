import React, { useEffect, useState } from 'react';
import axios from 'axios';

function Dashboard() {
  const [totalDonations, setTotalDonations] = useState(0);
  const [totalDonors, setTotalDonors] = useState(0);

  useEffect(() => {
    // Fetch dashboard statistics from the backend
    axios.get('http://localhost:8088/data/total-donations')
      .then(response => {
        setTotalDonations(response.data.total_donations);
      })
      .catch(error => {
        console.error('Error fetching total donations:', error);
      });

    axios.get('http://localhost:8088/data/number-of-donors')
      .then(response => {
        setTotalDonors(response.data.number_of_donors);
      })
      .catch(error => {
        console.error('Error fetching number of donors:', error);
      });


  }, []);

  return (
    <div className="dashboard-container">
      <h2>It's their home too!</h2>
      <p>Total Donations: ${totalDonations}</p>
      <p>Total Donors: {totalDonors}</p>
      
    </div>
  );
}

export default Dashboard;

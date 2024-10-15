import React, { useEffect, useState } from 'react';
import axios from 'axios';

function DonorDashboard() {

  const [topDonor, setTopDonor] = useState(null);

  useEffect(() => {
    // Fetch dashboard statistics from the backend
    

    axios.get('http://localhost:8088/data/top-donor')
      .then(response => {
        const topDonorData = response.data[0];  // Access the first object in the array
        setTopDonor({
          name: topDonorData.donor_name,
          amount: topDonorData.amount,
          email: topDonorData.email,
        });
      })
      .catch(error => {
        console.error('Error fetching top donor:', error);
      });

  }, []);

  return (
    <div className="dashboard-container">
      <h1 style={{color:"rgb(37, 22, 73)"}}>Hall Of Fame</h1>
      {topDonor ? (
        <div>
          <h2 style={{color: "rgb(37, 22, 73)"}}>Top Donor: </h2>{topDonor.name}
          <h2 style={{color: "rgb(37, 22, 73)"}}>Amount Donated: </h2> ${topDonor.amount}
          <h2 style={{color: "rgb(37, 22, 73)"}}>Email: </h2>{topDonor.email}
        </div>
      ) : (
        <p>Loading top donor details...</p>
      )}
    </div>
  );
}

export default DonorDashboard;

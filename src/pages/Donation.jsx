import React, { useState } from 'react';
import DonorDashboard from "../components/DonorDashboard";
import '../Donation.css'; // Ensure you create a CSS file for styles

function Donation() {
  const [donorName, setDonorName] = useState('');
  const [email, setEmail] = useState('');
  const [amount, setAmount] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Create a donation object to send to the backend
    const donationData = {
      donor_name: donorName,
      email: email,
      amount: parseFloat(amount), // Ensure amount is a number
    };

    try {
      // Make a POST request to your backend API
      const response = await fetch('http://localhost:8088/data/insert-donation', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(donationData),
      });

      // Check if the request was successful
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      // Optionally, you can process the response from the backend
      const result = await response.json();
      console.log(result);

      // Alert the user about the successful donation
      alert(`Thank you, ${donorName}, for your donation!`);

      // Reset form fields
      setDonorName('');
      setEmail('');
      setAmount('');
    } catch (error) {
      console.error('Error:', error);
      alert('There was an error processing your donation. Please try again.');
    }
  };

  return (
    <div className='home-container'>
    <div className="donation-container">
      <div className='title'>
        <h2>Make a Donation</h2>
      </div>
      <div className="donation-content">
        <div className="donation-form-section">
          <form className="donation-form" onSubmit={handleSubmit}>
            <input
              type="text"
              placeholder="Donor Name"
              value={donorName}
              onChange={(e) => setDonorName(e.target.value)}
              required
            />
            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            <input
              type="number"
              placeholder="Donation Amount"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              required
            />
            <button className='donate'type="submit">Donate</button>
          </form>
        </div>
        <div className="top-donor-section">
          <DonorDashboard />
        </div>
      </div>
    </div>
    </div>
  );
}

export default Donation;

import React from 'react';
import {useNavigate} from 'react-router-dom';
import Dashboard from '../components/Dashboard';

function Home() {
  const navigate = useNavigate();

  const navClick = () => {
    navigate('/donation'); // Navigate to the Donation page
  };
  return (
    <div className="home-container">
      <div style={{display:"flex"}}>
        <section className="about-section">
          <h1>Welcome to Zoodon</h1>
          <p>Help us save endangered species and contribute to their welfare.</p>
        </section>
        <div className='about-section'>
          <Dashboard />
          <div style={{display:"flex", alignContent: "center"}}>
            <button
            onClick={navClick} className='donate'>Donate Now</button>
          </div>
        </div>
      </div>
      <div className='about-section'>
      <p>At Zoodon, we are passionate about protecting and preserving our planet's wildlife for generations to come. Founded by a group of dedicated nature enthusiasts, our organization is committed to supporting conservation efforts for endangered species worldwide.
      </p>
          <h2>Our Mission</h2>

          <p>Our mission is to raise awareness about the critical state of wildlife and foster a community of compassionate individuals who share a common goal: the preservation of biodiversity. We believe that every small action counts, and through education, advocacy, and direct support, we aim to make a significant impact on the lives of animals and their habitats.
          </p>
          <h2>What We Do</h2>
          <p>
          <ul>
            <li>
            <h4>Conservation Initiatives: </h4> We collaborate with local and international conservation organizations to fund and support initiatives aimed at protecting endangered species and restoring their habitats.
            </li>
            <li><h4>Community Engagement: </h4>We organize workshops, educational programs, and awareness campaigns to inspire communities to take action towards wildlife conservation. By fostering a connection between people and nature, we empower individuals to make informed decisions that benefit our environment.
            </li>
            
            <li>
              <h4>Research and Advocacy:</h4> Our team actively participates in wildlife research projects and advocates for policies that protect threatened species and their habitats. We believe that informed advocacy is crucial for effecting positive change.
            </li>
          </ul>
          </p>
          <h2>Join Us</h2>
          <p>
          Whether you're a wildlife enthusiast, a concerned citizen, or someone looking to make a difference, Zoodon invites you to join our mission. Together, we can work towards a future where all species thrive in their natural habitats.

          Your support, whether through donations or volunteer work, plays a vital role in our efforts to create a sustainable future for wildlife. Join us in our journey to make a lasting impact on the planet and the incredible creatures that inhabit it.

          Thank you for being a part of Zoodon!
          </p>
      </div>
      
    </div>
  );
}

export default Home;

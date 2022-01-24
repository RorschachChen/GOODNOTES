#!/bin/bash
headless=true
verbose=false

if [ "$headless" = true ] ; then
    export DISPLAY=:0 XAUTHORITY=/var/run/lightdm/root/:0
fi

#Enable user defined fancontrol for all gpu
nvidia-settings -a "GPUFanControlState=1"

while true
do

    #gpu index
    i=0

    #Get GPU temperature of all cards
    for gputemp in $(nvidia-smi --query-gpu=temperature.gpu --format=csv,noheader);do
    
    if [ "$verbose" = true ] ; then
        echo "gpu ${i} temp ${gputemp}"; 
    fi

        #Note: you need to set the minimum fan speed to a non-zero value, or it won't work
        #This fan profile is being used in my GTX580 (Fermi). Change it as necessary

        #If temperature is between X to Y degrees, set fanspeed to Z value
        case "${gputemp}" in
                0[0-9])
                        newfanspeed="40"
                        ;;
                1[0-9])
                        newfanspeed="40"
                        ;;
                2[0-9])
                        newfanspeed="40"
                        ;;
                3[0-9])
                        newfanspeed="40"
                        ;;
                4[0-9])
                        newfanspeed="40"
                        ;;
                5[0-4])
                        newfanspeed="50"
                        ;;
                5[5-6])
                        newfanspeed="60"
                        ;;
                5[7-9])
                        newfanspeed="70"
                        ;;
                6[0-5])
                        newfanspeed="80"
                        ;;
                6[6-9])
                        newfanspeed="90"
                        ;;
                7[0-5])
                        newfanspeed="95"
                        ;;
                7[6-9])
                        newfanspeed="98"
                        ;;
                *)
                        newfanspeed="98"
                        ;;
        esac
        
        nvidia-settings -a "[fan-${i}]/GPUTargetFanSpeed=${newfanspeed}" 2>&1 >/dev/null
        
        if [ "$verbose" = true ] ; then
            echo "gpu ${i} new fanspeed ${newfanspeed}"; 
        fi
        
        sleep 3s
    #increment gpu index
    i=$(($i+1))
    done
done
